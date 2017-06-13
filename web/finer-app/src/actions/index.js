import queryString from 'query-string'

// Action type definitions.
export const ANNOTATE_TEXT = "ANNOTATE_TEXT"
export const RECEIVE_ANNOTATION = "RECEIVE_ANNOTATION"
export const SELECT_MENTION = "SELECT_MENTION"

const ANNOTATION_URL = "http://127.0.0.1:4567/annotate"

export const annotateText = text => ({
  type: ANNOTATE_TEXT,
  text
})


export const receiveAnnotation = (annotation) => ({
  type: RECEIVE_ANNOTATION,
  annotation
})

export const selectMention = (mentionId) => ({
  type: SELECT_MENTION,
  mentionId
})


export const fetchAnnotation = text => dispatch => {
  dispatch(annotateText(text))
  let req_url = `/annotate?${queryString.stringify({text: text})}`
  console.log(req_url)
  return fetch(
  	req_url,
  	 {method: 'GET'})
    .then(response => {
        console.log(response)

    	if (!response.ok) {
            throw Error(response.statusText);
        }
        console.log(response)
    	return response.json()
    })
    .then(json => dispatch(receiveAnnotation(json)))
    .catch(function(error) {
        console.log(error);
        dispatch(receiveAnnotation({}))
    });

}

function sleep(ms) {
  return new Promise(resolve => setTimeout(resolve, ms));
}

// export const fetchAnnotation = text => dispatch => {
// 	dispatch(annotateText(text))
// 	console.log("Fetching annotation...")
// 	console.log(text)
// 	sleep(2000).then( () => {
// 		let token = (start, end, types, selected = false) => ({
// 				start,
// 				end,
// 				types,
// 				selected
// 			})

// 		let new_types = (type_name, reasons = {}) => ({type_name, reasons})
// 		let reason = (evidence, comment) => ({evidence, comment})

// 		let action = receiveAnnotation({
// 			tokens : ["China", "invite", "Google", "Inc", "to", "join", "its", "conference"],
// 			mentions: [
// 				token(0,1,[new_types("person")], true),
// 				token(2,4,[new_types("organization", reason([1,2], "")),
// 						   new_types("organization.government"), reason("just comment")])
// 				]});
// 		console.log(action)
// 		dispatch(action)		
// 	})
// 	// TODO: Change back to the above one.
// }
