
def call() {
    def dockerx = new org.iti.docker()
    pipeline{
        agent{
            label "bash"
        }
        environment {
            DOCKER_CREDS = credentials('dockerhub-user')
        }

        stages{
            stage("build Docker image using python"){
                steps{
                    script{
                        def dockerx = new org.iti.docker()
                        dockerx.build("meldeeeb/python-app", "${BUILD_NUMBER}")
                    }
                
                }
            }
            stage("push Docker image using python"){
                steps{
                    script{
                        def dockerx = new org.iti.docker()
                        dockerx.login(env.DOCKER_CREDS_USR, env.DOCKER_CREDS_PSW)
                        dockerx.push("meldeeeb/python-app", "${BUILD_NUMBER}")

                    }
                }
            }
        }
    }
}
