# -*- coding: utf-8 -*-
"""
Created on Wed Sep 23 16:05:41 2020

@author: hp
"""
from urllib.request import urlopen
from datetime import datetime, timedelta
from urllib.error import URLError, HTTPError
import json 

def send_all_languages():
    d = datetime.today() - timedelta(days=30)
    d=d.strftime("%Y-%m-%d")
    #print(d)

    url='https://api.github.com/search/repositories?q=created:<'+d+'&sort=stars&order=desc&page=1&per_page=100'
    print ('URL:',url)
    try:
        data=urlopen(url)
    except HTTPError as e:
    # do something
        print('Error code: ', e.code)
        exit()
    except URLError as e:
    # do something
        print('Reason: ', e.reason)
        exit()
# else:
#     # do something
#     print('good!')

    parsedata=json.loads(data.read())
    #print(parsedata)
    languages=[]
    lang=[]
    compteur=1
#query on total_count
    for datap in parsedata['items']:
        if len(languages) == 0:
        #first item
            languages.append({'name': datap['language'],'nbcount': 1,'ordre_tendance':'1','items':[datap]})
            lang.append(datap['language'])
            compteur+=1
            continue
        if datap['language'] in lang:
            for l in languages:
                if(datap['language']==l['name']):
                    l['nbcount']+=1
                    l['ordre_tendance']+=","+str(compteur)
                    l['items'].append(datap)
                    break;
            compteur+=1
            continue
        else:
            languages.append({'name': datap['language'],'nbcount': 1,'ordre_tendance':str(compteur),'items':[datap]})
            lang.append(datap['language'])
            compteur+=1
  
  
 
    print(lang)
  
    print("\n \n")
    for lp in languages:
    # code...
        print("\n languages "+str(lp['name']))
        print("Tendance "+lp['ordre_tendance'])
        print(" Nb count "+str(len(lp['items'])))
        print("Nombre count "+str(lp['nbcount']))
    output=json.dumps(languages)
    with open('all_language.json', 'w') as f:
        print(output,file=f)
    
    
    
    
def send_specific_language(arg):
    d = datetime.today() - timedelta(days=30)
    d=d.strftime("%Y-%m-%d")
    #print(d)

    url='https://api.github.com/search/repositories?q=created:<'+d+'&sort=stars&order=desc&page=1&per_page=100'
    print ('URL:',url)
    try:
        data=urlopen(url)
    except HTTPError as e:
    # do something
        print('Error code: ', e.code)
        exit()
    except URLError as e:
    # do something
        print('Reason: ', e.reason)
        exit()
# else:
#     # do something
#     print('good!')

    parsedata=json.loads(data.read())
    compteur=1
    language={'name': arg,'nbcount': 0,'ordre_tendance':'','items':[]}
            
    for datap in parsedata['items']:
        if(datap['language']==arg):
    
            if(language['nbcount']==0):
                language['nbcount']+=1;
                language['ordre_tendance']=str(compteur)
                language['items'].append(datap)
            else:
        
                language['nbcount']+=1;
                language['ordre_tendance']+=","+str(compteur)
                language['items'].append(datap)
        compteur+=1
        # code...
    print("\n languages "+str(language['name']))
    print("Tendance "+language['ordre_tendance'])
    print(" Nb count "+str(len(language['items'])))
    print("Nombre count "+str(language['nbcount']))
    output=json.dumps(language)
    with open('language_abc.json', 'w') as f:
        print(output,file=f)
        
    
send_all_languages();

send_specific_language("Java")
