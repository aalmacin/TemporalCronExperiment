package helloworldapp;

import io.temporal.client.WorkflowClient;
import io.temporal.serviceclient.WorkflowServiceStubs;
import io.temporal.worker.Worker;
import io.temporal.worker.WorkerFactory;

public class HelloWorldWorker {

    public static void main(String[] args) {
        // This gRPC stubs wrapper talks to the local docker instance of the Temporal.
        WorkflowServiceStubs service = WorkflowServiceStubs.newLocalServiceStubs();
        WorkflowClient client = WorkflowClient.newInstance(service);
        // Create a Worker factory that can be used to create Workers that poll specific Task Queues.
        WorkerFactory factory = WorkerFactory.newInstance(client);
        // This Worker hosts both Workflow and Activity implementations.
        // Workflows are stateful, so you need to supply a type to create instances.
        Worker worker = factory.newWorker(Shared.HELLO_WORLD_TASK_QUEUE);
        // Activities are stateless and thread safe, so a shared instance is used.
        worker.registerWorkflowImplementationTypes(HelloWorldWorkflowImpl.class);
        worker.registerActivitiesImplementations(new FormatImpl());
        // Start polling the Task Queue.
        factory.start();
    }
}
