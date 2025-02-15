package io.github.jtsato.walletservice.archunit;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.CompositeArchRule;
import io.github.jtsato.walletservice.WalletsServiceApplication;
import org.slf4j.Logger;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.fields;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;
import static com.tngtech.archunit.library.GeneralCodingRules.*;

/**
 * @author Jorge Takeshi Sato
 */

@AnalyzeClasses(packagesOf = WalletsServiceApplication.class)
public class CodingRulesTest {

    @ArchTest
    private final ArchRule noAccessToStandardStreams = NO_CLASSES_SHOULD_ACCESS_STANDARD_STREAMS;

    @ArchTest
    private void noAccessToStandardStreams(final JavaClasses classes) {
        noClasses().should(ACCESS_STANDARD_STREAMS).check(classes);
    }

    @ArchTest
    private final ArchRule noGenericExceptions = NO_CLASSES_SHOULD_THROW_GENERIC_EXCEPTIONS;

    @ArchTest
    private final ArchRule noJavaUtilLogging = NO_CLASSES_SHOULD_USE_JAVA_UTIL_LOGGING;

    @ArchTest
    private final ArchRule loggersShouldBePrivateStaticFinal = fields().that()
                                                                       .haveRawType(Logger.class)
                                                                       .should()
                                                                       .bePrivate()
                                                                       .andShould()
                                                                       .beStatic()
                                                                       .andShould()
                                                                       .beFinal()
                                                                       .because("we agreed on this convention");
    @ArchTest
    private final ArchRule noJodaTime = NO_CLASSES_SHOULD_USE_JODATIME;

    @ArchTest
    static final ArchRule noClassesShouldAccessStandardStreamsOrThrowGenericExceptions = CompositeArchRule.of(NO_CLASSES_SHOULD_ACCESS_STANDARD_STREAMS)
                                                                                                        .and(NO_CLASSES_SHOULD_THROW_GENERIC_EXCEPTIONS);
}
