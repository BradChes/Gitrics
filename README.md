# Gitrics
## Introduction
To improve developers’ behaviours and increase improvements in the quality of commits to the ‘master’ Git branch. Gitrics is a API service that could be used to monitor the health of codebases that are stored in a remote repository.
## How to run
TODO
## Getting the results
Once the API service is up and running, the results can be displayed in two ways:
1. Using a dedicated UI front (see Projects dependant on Gitrics for more infomation).
2. Accessing the raw JSON data format at [localhost](http://localhost:8080/) followed by:
  -"/branches"
  -"/branches/feat"
  -"/branches/spike"
  -"/branches/fix"
  -"/branches/other"
  -"/branches/unmerged"
  -"/branches/merged"
  -"/branches/stale"
## Projects dependant on Gitrics
Currently there is one project that is dependent on the Gitrics API and that is the [Gitrics React App](https://github.com/bradches/gitrics-react).
