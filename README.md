# HisYar
## A history agent that uses an llm and a embedding model to answer questions about history!

You can use the GetEmbedding file to get the embedding vectors of lines of a file 
You can also choose to create a sentence for each [minLine-maxLine] of lines and the jump variable to specify how many lines to jump between every two sentences.
(Please note that it can take a lot of time.)

You can also use the ApiGet file to get some historical events from an api that gets them from wikipedia (already done and in the api.txt file).

For this one you need to have llama3.2:3b (you can change the size in the code.) and nomic-embed-text (you can change this too (not tested)).

And you need json-simple-1.1.
