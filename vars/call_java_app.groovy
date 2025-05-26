

def call() {
    def dockerx = new org.iti.docker(this)
    pipeline{
        agent {
            label 'bash'
        }

        environment {
            DOCKER_CREDS = credentials('dockerhub-user')
        }

        stages{
            stage("Build java app"){
                steps{
                    sh "mvn clean package install"
                }
            }
            stage("build java app image"){
                steps{
                    script{
                    
                        dockerx.build("meldeeeb/java-app", "${BUILD_NUMBER}")
                    }
                }
            }
            stage("push java app image"){
                steps{
                    script{
                      
                        dockerx.login(env.DOCKER_CREDS_USR, env.DOCKER_CREDS_PSW)
                        dockerx.push("meldeeeb/java-app", "${BUILD_NUMBER}")
                    }
                }
            }
        }
    }
}