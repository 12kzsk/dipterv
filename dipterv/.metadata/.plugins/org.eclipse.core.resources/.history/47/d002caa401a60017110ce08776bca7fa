package tech.artisanhub.EventProcessor;

import java.util.Collection;

import org.drools.core.impl.KnowledgeBaseFactory;
import org.drools.core.marshalling.impl.ProtobufMessages.KnowledgeBase;
import org.kie.api.definition.KiePackage;
import org.kie.api.io.ResourceType;
import org.kie.internal.builder.KnowledgeBuilder;
import org.kie.internal.builder.KnowledgeBuilderFactory;
import org.kie.internal.io.ResourceFactory;
import org.kie.internal.runtime.StatefulKnowledgeSession;

public class EventProcessor {

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
