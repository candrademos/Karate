function test () {
    return `{
     'query': '{
         allFilms {
             totalCount
             films {
                 title
                 director
                 releaseDate
                 created
                 episodeID
             }
         }
     }'
   }`
}