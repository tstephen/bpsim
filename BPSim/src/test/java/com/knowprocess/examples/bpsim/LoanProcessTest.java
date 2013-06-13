package com.knowprocess.examples.bpsim;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.test.ActivitiRule;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;

import com.knowprocess.resource.spi.Fetcher;
import com.knowprocess.xslt.TransformTask;

public class LoanProcessTest {

    private static final String KP_BPMN = "kp-loan.bpmn";
    private static final String APPROVER = "Tim";
    private static final int DEFAULT_PRIORITY = 50;

    private static final String BPSIM_ORIGINAL = "Loan Process v1.0.bpmn";
    private static final String BPSIM_MOD = "Loan Process v1.0-knowprocess.bpmn";

    @Rule
    public ActivitiRule activitiRule = new ActivitiRule("test-activiti.cfg.xml");

    @Test
    public void testActivitiCompatible() {
        TransformTask transformTask = new TransformTask();
        transformTask.setXsltResource("/xslt/ActivitiSupportRules.xsl");
        try {
            String result = transformTask
                    .transform(getBpmnAsString(BPSIM_ORIGINAL));
            System.out.println("result: " + result);
            String[] msgs = result.split("\n");
            for (String msg : msgs) {
                assertTrue(!msg.trim().startsWith("ERROR"));
            }
        } catch (IOException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    @Test
    public void testKnowProcessCompatible() {
        try {
            List<String> errors = getKnowProcessCompatibilityErrors(getBpmnAsString(BPSIM_ORIGINAL));
            assertEquals("Unexpected number of errors reported", 0,
                    errors.size());
        } catch (IOException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    private List<String> getKnowProcessCompatibilityErrors(String bpmn) {
        TransformTask transformTask = new TransformTask();
        transformTask.setXsltResource("/xslt/KpSupportRules.xsl");
        String result = transformTask.transform(bpmn);
        System.out.println("result: " + result);
        String[] msgs = result.split("\n");
        List<String> errors = new ArrayList<String>();
        for (String msg : msgs) {
            if (msg.trim().startsWith("ERROR")) {
                errors.add(msg);
            }
        }
        return errors;
    }

    private String getBpmnAsString(String bpmnFile) throws IOException {
        return new Fetcher().fetchToString("classpath:///" + bpmnFile);
    }

    @Test
    @Ignore
    // Fails even after adding the usual condition resource and timer
    // expressions because don't have support for throw intermediate msg event
    public void testBPSimOriginalInKnowProcess() {
        tweakAndRunInKnowProcess(BPSIM_ORIGINAL, "_6");
    }

    @Test
    public void testBPSimModifiedInKnowProcess() {
        tweakAndRunInKnowProcess(BPSIM_MOD, "_6");
    }

    private void tweakAndRunInKnowProcess(String bpmnResource, String procDefKey) {
        TransformTask transformTask = new TransformTask();
        transformTask.setXsltResource("/xslt/ExecutableTweaker.xsl");
        try {
            String result = transformTask
                    .transform(getBpmnAsString(bpmnResource));
            // System.out.println("result: " + result);
            PrintWriter out = null;
            try {
                out = new PrintWriter(new File(KP_BPMN));
                out.println(result);
            } catch (Exception e) {
                fail(e.getMessage());
            } finally {
                out.close();
            }

            List<String> errors = getKnowProcessCompatibilityErrors(result);
            assertEquals("Should not have any incompatibilities at this point",
                    0, errors.size());

            RepositoryService repositoryService = activitiRule
                    .getRepositoryService();
            Deployment deployment = repositoryService.createDeployment()
                    .addString(KP_BPMN, result).deploy();
            System.out.println("deployment: " + deployment);
            RuntimeService runtimeService = activitiRule.getRuntimeService();
            Map<String, Object> variableMap = new HashMap<String, Object>();
            variableMap.put("initiator", APPROVER);

            List<Deployment> deployments = activitiRule.getRepositoryService()
                    .createDeploymentQuery().list();
            for (Deployment d : deployments) {
                System.out.println("deployment: " + d.getName() + "("
                        + d.getId() + ")");
            }

            ProcessInstance processInstance = runtimeService
                    .startProcessInstanceByKey(procDefKey, variableMap);
            assertNotNull(processInstance.getId());
            System.out.println("id " + processInstance.getId() + " "
                    + processInstance.getProcessDefinitionId());
            activitiRule.dumpProcessState(processInstance.getId(),
                    new PrintWriter(System.out));

            // assign approver
            // Task assignApprover = activitiRule.assertAssignedTaskExists(
            // "Assign Approver", teamAssistant, DEFAULT_PRIORITY);
            // variableMap.put("approver", APPROVER);
            // activitiRule.getTaskService().complete(assignApprover.getId(),
            // variableMap);

            // activitiRule.assertComplete(processInstance.getId());
        } catch (IOException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }
}