import React, { Component } from 'react';
import { connect } from 'react-redux'
import AnnotationInput from "../components/annotation_input.js"
import AnnotationResult from "../components/annotation_result.js"
import {fetchAnnotation, selectMention} from "../actions"
import LinearProgress from 'material-ui/LinearProgress';
import AppBar from 'material-ui/AppBar';

import PropTypes from 'prop-types'

class AnnotationAppContainer extends Component {

  static propTypes = {
    loading: PropTypes.bool.isRequired,
    annotation: PropTypes.object,
    dispatch: PropTypes.func.isRequired
  }

  handleAnnotationRequest(new_text){
    const { dispatch} = this.props
    dispatch(fetchAnnotation(new_text))
  }

  handleSelectedMention(mention){
    const { dispatch} = this.props
    console.log(mention)
    dispatch(selectMention(mention.start))
  }

  render() {
    return (
      <div>
          <AppBar
            title="Fine Grain Entity Typing Demo"
            showMenuIconButton={false}
            iconClassNameRight="muidocs-icon-navigation-expand-more"
          />

        <AnnotationInput 
          value="" 
          lock = {this.props.loading}
          onClick={this.handleAnnotationRequest.bind(this)} />  
        <AnnotationResult 
          value={this.props.annotation}
          selected={this.props.mentionId}
          onClick={this.handleSelectedMention.bind(this)} />  
      </div>
 	 );
  }
}


const mapStateToProps = state => {
  const { loading, annotation } = state
  return {
    loading:state.get('loading'),
    text:state.get('text'),
    mentionId:state.get('mentionId'),
    annotation:state.get('annotation')
  }
}

export default connect(mapStateToProps)(AnnotationAppContainer)
