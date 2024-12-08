package me.toidicakhia.viastar.model

data class JenkinsJob(
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