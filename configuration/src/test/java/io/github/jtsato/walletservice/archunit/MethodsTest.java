package io.github.jtsato.walletservice.archunit;

import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import io.github.jtsato.walletservice.WalletsServiceApplication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ResponseStatus;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.methods;

@AnalyzeClasses(packagesOf = WalletsServiceApplication.class)
public class MethodsTest {

    @ArchTest
    static ArchRule allPublicMethodsInControllerLayerShouldReturnResponseStatus =
            methods().that()
                    .areDeclaredInClassesThat()
                    .haveSimpleNameEndingWith("Controller")
                    .and()
                    .arePublic()
                    .should()
                    .beAnnotatedWith(ResponseStatus.class);

    @ArchTest
    static ArchRule codeUnitsInDataProviderLayerShouldBeTransactional =
            classes().that()
            .haveSimpleNameEndingWith("Provider")
            .should()
            .beAnnotatedWith(Transactional.class);
}
