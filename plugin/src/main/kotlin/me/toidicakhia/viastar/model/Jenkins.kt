package me.toidicakhia.viastar.model

data class JenkinsLastSuccessBuild(
	val lastSuccessfulBuild: JenkinsJobMeta
)

data class JenkinsJobMeta(
	val number: Int,
	val url: String
)

data class JenkinsBuild(
	val artifacts: List<JenkinsArtifact>
)

data class JenkinsArtifact(
	val fileName: String,
	val relativePath: String
)

data class Jenkins(
	val jobs: List<Job>
)

data class Job(
	val name: String,
	val url: String
)

data class JobsMetadata(
	val jobs: Map<String, Int>
)