package io.github.jtsato.walletservice.archunit;

import com.tngtech.archunit.junit.AnalyzeClasses;
import io.github.jtsato.walletservice.WalletsServiceApplication;

/**
 * @author Jorge Takeshi Sato
 */

@AnalyzeClasses(packagesOf = WalletsServiceApplication.class)
public class MethodsTest {

    /*
    @ArchTest
    static ArchRule all_public_methods_in_the_controller_layer_should_return_ResponseStatus =
            methods().that()
                    .areDeclaredInClassesThat()
                    .haveSimpleNameEndingWith("Controller")
                    .and()
                    .arePublic()
                    .should()
                    .beAnnotatedWith(ResponseStatus.class);
    */
    /*
    @ArchTest
    static ArchRule code_units_in_Infra_layer_should_be_Transactional =
            classes().that()
            .haveSimpleNameEndingWith("DatabaseProvider")
            .should()
            .beAnnotatedWith(Transactional.class);
     */
}
