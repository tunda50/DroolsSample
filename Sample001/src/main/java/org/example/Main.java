package org.example;

import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.KieModule;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.internal.io.ResourceFactory;

public class Main {

    public static final String drlFile = "myRule.drl";
    public static void main(String[] args) {
        // Load rules from classpath
        KieContainer kContainer = kieContainer();
        KieSession kSession = kContainer.newKieSession();
        // Create a person object and insert it into the session
        Person person = new Person("John", 65);
        kSession.insert(person);

        // Fire the rules
        kSession.fireAllRules();
        // Print the person's discount
        System.out.println("Person: " + person.getName() + ", Discount: " + person.getDiscount());
        // Dispose the session
        kSession.dispose();
    }

    public static KieContainer kieContainer() {
        KieServices kieServices = KieServices.Factory.get();

        KieFileSystem kieFileSystem = kieServices.newKieFileSystem();
        kieFileSystem.write(ResourceFactory.newClassPathResource(drlFile));
        KieBuilder kieBuilder = kieServices.newKieBuilder(kieFileSystem);
        kieBuilder.buildAll();
        KieModule kieModule = kieBuilder.getKieModule();

        return kieServices.newKieContainer(kieModule.getReleaseId());

    }
}