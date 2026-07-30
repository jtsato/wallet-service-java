# ==========================================
# 1. Base Stage (Runtime)
# ==========================================
FROM eclipse-temurin:25-jre-alpine AS base
WORKDIR /app

# ==========================================
# 2. Build Stage (Multi-stage)
# ==========================================
FROM maven:3.9-eclipse-temurin-25-alpine AS build
WORKDIR /source

# Cache optimization: copy dependency descriptors first
COPY pom.xml ./
COPY configuration/pom.xml ./configuration/
COPY core/pom.xml ./core/
COPY entrypoints/rest/pom.xml ./entrypoints/rest/
COPY infra/database/pom.xml ./infra/database/

# Download Maven plugins before building the reactor.
# Dependencies are resolved during the reactor build because some are local modules.
RUN mvn dependency:resolve-plugins -B

# Copy the remaining source code and build the application
COPY . .
RUN mvn clean package -DskipTests -pl configuration -am

# ==========================================
# 3. Final Stage (Production)
# ==========================================
FROM base AS final
WORKDIR /app

# Important environment variables
ENV JAVA_TOOL_OPTIONS="-XX:MaxRAMPercentage=75.0 -XX:+UseG1GC" \
    SERVER_PORT=8081

# Copy the executable JAR generated in the build stage
COPY --from=build /source/configuration/target/walletservice-starter.jar app.jar

EXPOSE 8081

# Create a non-root user with minimal permissions
RUN addgroup -g 2000 ragnarok && \
    adduser -u 1000 -G ragnarok -s /bin/sh -D ragnarok && \
    chown -R 1000:2000 /app

USER 1000:2000

# Healthcheck using wget, which is native to Alpine
HEALTHCHECK --interval=30s --timeout=5s --start-period=30s --retries=3 \
  CMD wget -q --spider http://localhost:8081/actuator/health || exit 1

ENTRYPOINT ["java", "-jar", "app.jar"]
