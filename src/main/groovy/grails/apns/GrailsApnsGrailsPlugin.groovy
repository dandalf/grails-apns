package grails.apns

import grails.plugins.*
import grails.util.Environment
import org.codehaus.groovy.control.ConfigurationException
import org.epseelon.grails.apns.ApnsFactoryBean

class GrailsApnsGrailsPlugin extends Plugin {

    // the version or versions of Grails the plugin is designed for
    def grailsVersion = "3.3.2 > *"
    // resources that are excluded from plugin packaging
    def pluginExcludes = [
        "grails-app/views/error.gsp"
    ]

    def title = "Apple Push Notification Service Plugin" // Headline display name of the plugin
    def author = "Daniel Rusk"
    def authorEmail = "djrusk@gmail.com"
    def description = '''\
Integrates with Apple Push Notification service to send push notifications to an iPhone client of your application
'''

    def developers = [
            [name: 'Sebastien Arbogast', email: 'sebastien.arbogast@gmail.com'],
            [name: 'Arthur Neves', email: 'arthurnn@gmail.com']
    ]

    def issueManagement = [url: 'https://github.com/dandalf/grails-apns/issues']
    def scm = [url: 'https://github.com/dandalf/grails-apns']

    def profiles = ['web']

    // URL to the plugin's documentation
    def documentation = "http://grails.org/plugin/grails-apns"

    // Extra (optional) plugin metadata

    // License: one of 'APACHE', 'GPL2', 'GPL3'
    def license = "GPL3"


    Closure doWithSpring() {
        { ->
            def conf = config.apns

            ApnsFactoryBean.Environment apnsConfigEnvironment
            if (conf.environment) {
                apnsConfigEnvironment = ApnsFactoryBean.Environment.valueOf(conf.environment.toString().toUpperCase())
            } else {
                if (Environment.current == Environment.PRODUCTION) {
                    apnsConfigEnvironment = ApnsFactoryBean.Environment.PRODUCTION
                } else {
                    apnsConfigEnvironment = ApnsFactoryBean.Environment.SANDBOX
                }
            }

            if(!conf.password){
                throw new ConfigurationException("Missing password for apns")
            }

            apnsService(ApnsFactoryBean) {
                pathToCertificate = conf.pathToCertificate ?: null
                certificateResourcePath = conf.certificateResourcePath ?: null
                password = conf.password
                apnsEnvironment = apnsConfigEnvironment
            }
        }
    }
}
