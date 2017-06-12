import { combineReducers } from 'redux'
import {
  ANNOTATE_TEXT, RECEIVE_ANNOTATION,
  SELECT_MENTION
} from '../actions'

import { Map } from 'immutable';

const INIT_STATE = Map({
	loading:false,
	text:"",
  mentionId:-1,
	annotation:{}})
// const annotateText = (state = false, action) => {
//   switch (action.type) {
//     case ANNOTATE_TEXT:
//       return true
//     default:
//       return state
//   }
// }

// const updateAnnotation = (state = INIT_ANNOTATION, action) => {
//   switch (action.type) {
//     case RECEIVE_ANNOTATION:
//       return action.annotation
//     default:
//       return state
//   }
// }

const loadingAnnotation = (state = INIT_STATE, action) => {
  switch (action.type) {
    case RECEIVE_ANNOTATION:   		
      return state.set('annotation', action.annotation)
       			.set('loading', false);
    case ANNOTATE_TEXT:
			return state.set('annotation', {})
				.set('text', action.text)
       			.set('loading', true);
    case SELECT_MENTION:
      return state.set('mentionId', action.mentionId);
   default:
      return state
  }
}

// const rootReducer = combineReducers({
//   annotateText,
//   updateAnnotation
// })

export default loadingAnnotation