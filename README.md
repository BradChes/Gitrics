# Gitrics
## Introduction
To improve developers’ behaviours and increase improvements in the quality of commits to the ‘master’ Git branch. Gitrics is a API service that could be used to monitor the health of codebases that are stored in a remote repository.
## How to run
When running the API service, it needs to be given a `config.json` file that looks like this:
```
{
	"username": [GITHUB ACCOUNT],
	"accessToken": [GITHUB ACCESS TOKEN],
	"repoUrl": [ADDRESS TO REPOSITORY],
	"branchMinimum": [MINIMUM NUMBER OF BRANCHES BEFORE WARNING],
	"branchMaximum": [MAXIMUM NUMBER OF BRANCHES BEFORE ERROR]
}
```
## Getting the results
Once the API service is up and running, the results can be displayed in two ways:
### Using a dedicated UI front 
(see Projects dependant on Gitrics for more infomation).
### Accessing the raw JSON data format 
Going to the [localhost](http://localhost:8080/) followed by these endpoints:
  - `/branches`
  	* Displays all the branches related to the repository.
  - `/branches/feat`
  	* Displays all the feature branches related to the repository.
  - `/branches/spike`
  	* Displays all the spike branches related to the repository.
  - `/branches/fix`
   	* Displays all the fix branches related to the repository.
  - `/branches/other`
  	* Displays all the other branches related to the repository.
  - `/branches/unmerged`
  	* Displays all the unmerged branches related to the repository.
  - `/branches/merged`
  	* Displays all the merged branches related to the repository.
  - `/branches/stale`
  	* Displays all the stale branches related to the repository.
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
Talking through the JSON response here is what each key means:
- `Branches`
	* JSON Array of Branch objects.
- `name`
	* The name of the branch on the repository.
- `firstCreation`
	* The time of which the branch was first created. This will appear null if the branch has already been merged.
- `lastCommit`
	* The time of the last commit to the branch.
- `stale`
	* Compares the days between `firstCreation` and `lastCommit`. If more than thirty days, this is set within code, the branch is considered stale.
- `merged`
	* Returns either true or false as to whether the branch has been merged into the repository.
- `size`
	* This returns an overall size of the branches object which is returned.

## Projects dependant on Gitrics
Currently there is one project that is dependent on the Gitrics API and that is the [Gitrics React App](https://github.com/bradches/gitrics-react).
