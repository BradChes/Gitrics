# Gitrics
## Introduction
To improve developers’ behaviours and increase improvements in the quality of commits to the ‘master’ Git branch. Gitrics is a API service that could be used to monitor the health of codebases that are stored in a remote repository.
## How to run
When running the API service, it needs to be given a `config.json` file that looks like this:
```
{
	"username": [GITHUB ACCOUNT],
	"accessToken": [GITHUB ACCESS TOKEN],
	"repoUrl": [ADDRESS TO REPOSITORY]
}
```
## Getting the results
Once the API service is up and running, the results can be displayed in two ways:
1. Using a dedicated UI front (see Projects dependant on Gitrics for more infomation).
2. Accessing the raw JSON data format at [localhost](http://localhost:8080/) followed by:
  *"/branches"
  *"/branches/feat"
  *"/branches/spike"
  *"/branches/fix"
  *"/branches/other"
  *"/branches/unmerged"
  *"/branches/merged"
  *"/branches/stale"
## Results Example
Here is an example of the results that you would get from hitting the [branches](http://localhost:8080/branches) endpoint:
```
{
	"branches": [
		{
			"name": "refs/remotes/origin/feat/JGit-service",
			"firstCreation": "null",
			"lastCommit": "null",
			"stale": false,
			"merged": true
		},
		{
			"name": "refs/remotes/origin/feat/RESTful-web-service-with-spring-boot",
			"firstCreation": "null",
			"lastCommit": "null",
			"stale": false,
			"merged": true
		},
		{
			"name": "refs/remotes/origin/feat/get-when-braches-were-first-created",
			"firstCreation": "Tue Oct 16 09:57:09 BST 2018",
			"lastCommit": "Thu Oct 18 09:30:29 BST 2018",
			"stale": true,
			"merged": false
		}
	],
	"size": 3
}
```
## Projects dependant on Gitrics
Currently there is one project that is dependent on the Gitrics API and that is the [Gitrics React App](https://github.com/bradches/gitrics-react).
