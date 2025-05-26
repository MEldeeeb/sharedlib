def call() {
    pipeline {
        agent {
            label "bash"
        }

        environment {
            DOCKER_CREDS = credentials('dockerhub-user')
        }

        stages {
            stage("build Docker image using python") {
                steps {
                    script {
                        def dockerx = new org.iti.docker(this)
                        dockerx.build("meldeeeb/python-app", "${BUILD_NUMBER}")
                    }
                }
            }

            stage("push Docker image using python") {
                steps {
                    script {
                        def dockerx = new org.iti.docker(this)
                        dockerx.login(env.DOCKER_CREDS_USR, env.DOCKER_CREDS_PSW)
                        dockerx.push("meldeeeb/python-app", "${BUILD_NUMBER}")
                    }
                }
            }
        }
    }
}