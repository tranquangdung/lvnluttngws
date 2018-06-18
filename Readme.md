1) Install Vietnamese plugin for Elasticsearch 2.4.0 using command below:
    G:\15.Proj\tools\elasticsearch-2.4.0\bin>plugin install file:D:/15.Proj/final/elasticsearch-analysis-vietnamese-2.4.0.zip

2) Import documents from specific folder
    a) API: http://localhost:8080/api/importdoc
    b) Request
        {
             "url": "D:\\15.Prj\\data"
        }
    c) Response
        OK: If result is success
        FAIL: Otherwise
    
2) Search
    a) Input format
    {
        "keywords": [
            "Việt Nam", "Châu Á"
            ],
        "indexFrom": 0,
        "indexTo": 10
    }

b) Response format
    {
        "result": [
            {
                "id": "2",
                "highLight": [
                    "<span style='background-color: #FFFF00'>Việt</span> <span style='background-color: #FFFF00'>Nam</span> là một nước nông nghiệp"
                ],
                "filePath": null,
                "score": 0.53033006
            },
            {
                "id": "1",
                "highLight": [
                    "<span style='background-color: #FFFF00'>Việt</span> <span style='background-color: #FFFF00'>Nam</span> là một nước nông nghiệp"
                ],
                "filePath": null,
                "score": 0.53033006
            }
        ]
    }

3) Add new document
a) Input format
{
	"fileName": "Document.pdf",
	"filePath": "D:\\Test\\Document1.pdf",
	"content": "Data test"
}

b) Response string
- OK
- FAIL