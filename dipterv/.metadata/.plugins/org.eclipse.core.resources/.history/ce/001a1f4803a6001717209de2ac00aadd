package com.sample;

import java.util.Collection;

/**
 * This is a sample class to launch a rule.
 */
public class DroolsTest {

	public static void main(String[] args) {
		final KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();

		// this will parse and compile in one step
		kbuilder.add(ResourceFactory.newClassPathResource("HelloWorld.drl",
		        HelloWorldExample.class), ResourceType.DRL);

		// Check the builder for errors
		if (kbuilder.hasErrors()) {
		    System.out.println(kbuilder.getErrors().toString());
		    throw new RuntimeException("Unable to compile \"HelloWorld.drl\".");
		}


		// get the compiled packages (which are serializable)
		final Collection<KiePackage> pkgs = kbuilder.getKnowledgePackages();

		// add the packages to a knowledgebase (deploy the knowledge packages).
		final KnowledgeBase kbase = KnowledgeBaseFactory.newKnowledgeBase();
		kbase.addKnowledgePackages(pkgs);
		final StatefulKnowledgeSession ksession = kbase.newStatefulKnowledgeSession();
	}

}
