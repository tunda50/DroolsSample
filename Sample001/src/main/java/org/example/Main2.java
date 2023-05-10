package org.example;

import org.apache.commons.io.IOUtils;
import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.KieModule;
import org.kie.api.io.Resource;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.internal.io.ResourceFactory;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class Main2 {

    //public static final String drlFile = "myRule.drl";
    public static final String drlFile = "/myRule.txt";
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
        //kieFileSystem.write(ResourceFactory.newClassPathResource(drlFile));
//        KieBuilder kieBuilder = kieServices.newKieBuilder(kieFileSystem);
//        kieBuilder.buildAll();
//        KieModule kieModule = kieBuilder.getKieModule();

        try (InputStream inputStream = Main2.class.getResourceAsStream(drlFile)) {
            String drlCode = IOUtils.toString(inputStream, Charset.defaultCharset());
            Resource resource = ResourceFactory.newByteArrayResource(drlCode.getBytes());
            resource.setTargetPath("rules/myRule.drl");
            kieFileSystem.write(resource);
            KieBuilder kieBuilder = kieServices.newKieBuilder(kieFileSystem);
            kieBuilder.buildAll();
            KieModule kieModule = kieBuilder.getKieModule();
            return kieServices.newKieContainer(kieModule.getReleaseId());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }



    }
}