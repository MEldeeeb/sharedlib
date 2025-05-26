def call() {
    pipeline{
        agent{
            label "bash"
        }

        environment {
            DOCKER_CREDS = credentials('dockerhub-user')
        }
        
        stages{
           
            stage("build Docker image"){
                steps{
                    sh "docker build -t meldeeeb/l_2:v${BUILD_NUMBER} ."
                }
            } 
            stage("Login to Docker Hub"){
                steps{
                    sh "docker login -u ${DOCKER_CREDS_USR} -p ${DOCKER_CREDS_PSW}"
                }
            }
            stage("Push Docker image"){
                steps{
                    sh "docker push meldeeeb/l_2:v${BUILD_NUMBER}"
                }
            }
        }
    }
}