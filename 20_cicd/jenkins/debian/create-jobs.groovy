pipelineJob('test-app') {
    definition {
        cpsScm {
            scm {
                git {
                    remote {
                        url 'https://github.com/endryha/spring-boot-test-api.git'
                    }
                    branch 'master'
                }
            }
        }
    }
}