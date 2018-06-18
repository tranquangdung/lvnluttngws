1) Search
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

2) Add new document
a) Input format
{
	"fileName": "Document.pdf",
	"filePath": "D:\\Test\\Document1.pdf",
	"content": "Data test"
}

b) Response string
- OK
- FAIL