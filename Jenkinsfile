pipeline {
    agent any

	stages {
		stage('Maven build') {
		}
		
		stage('Reporting') {
		}
		
		stage('Tag') {
		}
		
		stage('Push to Nexus') {
		}
		
	}

	post {
		failure {
			emailext body: "The build of the LET Application (${env.JOB_NAME}) failed! See ${env.BUILD_URL}", recipientProviders: [[$class: 'CulpritsRecipientProvider']], subject: 'LET Application Build Failure'
		}
	}
	options {
		timeout(time: 10, unit: 'MINUTES')
		buildDiscarder(logRotator(artifactDaysToKeepStr: '', artifactNumToKeepStr: '', daysToKeepStr: '', numToKeepStr: '7'))
	}
    triggers {
        pollSCM('H/5 * * * *')
    }  
	   
}    