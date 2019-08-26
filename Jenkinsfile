pipeline {
    agent {
        docker {
            image 'slickqa/slick-webdriver-java-build'
            args '-v /data/jenkins/.gnupg:/.gnupg -v /data/docker/maven:/.m2 -e MAVEN_OPTS="-Dmaven.repo.local=/.m2/repository -Duser.home=/"'
        }
    }
    stages {
        stage('Build') {
            steps {
                sh '''
                    POM_BUILD_NUMBER=${BUILD_NUMBER}
                    if [ "$BRANCH_NAME" != "master" ]; then POM_BUILD_NUMBER="${BRANCH_NAME}-${BUILD_NUMBER}"; fi

                    ORIGINAL_VERSION=$(mvn -q -DforceStdout help:evaluate -Dexpression='project.version')
                    NEW_VERSION=$(echo ${ORIGINAL_VERSION} | sed -e "s/SNAPSHOT/${POM_BUILD_NUMBER}/")

                    mvn -B versions:set -DnewVersion=${NEW_VERSION}
                    mvn -q -DforceStdout help:evaluate -Dexpression='settings.localRepository' && echo
                '''
                sh 'mvn -B -DskipTests clean package'
            }
        }
        stage('Deploy') {
            when {
                branch 'master'
            }
            steps {
                sh 'mvn -DskipTests clean install deploy -DautoReleaseAfterClose=true'
            }
        }
    }
}
