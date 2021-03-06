package org.gradle.profiler;

import java.io.File;
import java.io.PrintStream;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class GradleScenarioDefinition extends ScenarioDefinition {

    private final Invoker invoker;
    private final GradleBuildConfiguration buildConfiguration;
    private final Class<?> toolingModel;
    private final List<String> cleanupTasks;
    private final List<String> tasks;
    private final List<String> gradleArgs;
    private final Map<String, String> systemProperties;

    public GradleScenarioDefinition(String name, Invoker invoker, GradleBuildConfiguration buildConfiguration, List<String> tasks, Class<?> toolingModel, List<String> cleanupTasks, List<String> gradleArgs, Map<String, String> systemProperties, Supplier<BuildMutator> buildMutator, int warmUpCount, int buildCount, File outputDir) {
        super(name, buildMutator, warmUpCount, buildCount, outputDir);
        this.invoker = invoker;
        this.tasks = tasks;
        this.buildConfiguration = buildConfiguration;
        this.toolingModel = toolingModel;
        this.cleanupTasks = cleanupTasks;
        this.gradleArgs = gradleArgs;
        this.systemProperties = systemProperties;
    }

    @Override
    public String getDisplayName() {
        return getName() + " using " + buildConfiguration.getGradleVersion();
    }

    @Override
    public String getProfileName() {
        return getName() + "-" + buildConfiguration.getGradleVersion().getVersion();
    }

    @Override
    public String getBuildToolDisplayName() {
        return buildConfiguration.getGradleVersion().getVersion();
    }

    @Override
    public String getTasksDisplayName() {
        return tasks.stream().collect(Collectors.joining(" "));
    }

    public List<String> getGradleArgs() {
        return gradleArgs;
    }

    public Invoker getInvoker() {
        return invoker;
    }

    public List<String> getTasks() {
        return tasks;
    }

    public Class<?> getToolingModel() {
        return toolingModel;
    }

    public List<String> getCleanupTasks() {
        return cleanupTasks;
    }

    public GradleBuildConfiguration getBuildConfiguration() {
        return buildConfiguration;
    }

    public Map<String, String> getSystemProperties() {
        return systemProperties;
    }

    @Override
    protected void printDetail(PrintStream out) {
        out.println("  " + getBuildConfiguration().getGradleVersion() + " (" + getBuildConfiguration().getGradleHome() + ")");
        out.println("  Run using: " + getInvoker());
        out.println("  Cleanup Tasks: " + getCleanupTasks());
        out.println("  Tasks: " + getTasks());
        if (toolingModel != null) {
            out.println("  Tooling model: " + getToolingModel());
        }
        out.println("  Gradle args: " + getGradleArgs());
        if (!getSystemProperties().isEmpty()) {
            out.println("  System properties:");
            for (Map.Entry<String, String> entry : getSystemProperties().entrySet()) {
                out.println("    " + entry.getKey() + "=" + entry.getValue());
            }
        }
    }
}
