from github import Github
from datetime import datetime, timedelta

d = datetime.today() - timedelta(days=30)

g=Github("username","password")
print(d)
repositories = g.search_repositories(query='created:<2020-09-01',sort='stars',order='desc').get_page(1)
print('sizeof repository egale :',len(repositories))
for repo in repositories:
    print(repo)
    
#Can't be be long term solution  because of this ussie  https://github.com/PyGithub/PyGithub/issues/145
#