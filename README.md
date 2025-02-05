# HisYar
## A history agent that uses an llm and a embedding model to answer questions about history!

You can use the GetEmbedding file to get the embedding vectors of lines of a file.

You can also choose to create a sentence for each [minLine-maxLine] of lines and the jump variable to specify how many lines to jump between every two sentences.

for api the recommended settings is minyear=1, maxLine=1, jump=1

for book1 the recommended settings is minLine=4, maxLine=5, jump=2

for book2 the recommended settings is minLine=2, maxLine=3, jump=1

(Please note that it can take a lot of time without a discrete gpu.)

You can also use the ApiGet file to get some historical events from an api that gets them from wikipedia (already done and in the api.txt file).

For this one you need to have llama3.2:3b (you can change the model in the code (Main.llmModel). you can use qwen2.5 (0.5b, 1.5b, 3b, 7b, ...) you can use llama3.2:1b (1b) or deepseek-r1 (1.5b, 7b, 8b, ...)) 

And nomic-embed-text (you can change this too (Main.embeddingModel) (some examples if you want smaller models are : all-minilm (33m), all-minilm:22m (22m) and if you want a larger model mxbai-embed-large (335m)).

And you need json-simple-1.1 (or other versions (not tested)).
