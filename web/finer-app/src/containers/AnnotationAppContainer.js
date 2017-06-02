import React, { Component } from 'react';
import { connect } from 'react-redux'
import AnnotationInput from "../components/annotation_input.js"
import AnnotationResult from "../components/annotation_result.js"
import {fetchAnnotation} from "../actions"
import LinearProgress from 'material-ui/LinearProgress';

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

  render() {
    return (
      <div>
        <AnnotationInput 
          value="" 
          lock = {this.props.loading}
          onClick={this.handleAnnotationRequest.bind(this)} />  
        <AnnotationResult 
          value={this.props.annotation}
          onClick={console.log} />  
      </div>
 	 );
  }
}


const mapStateToProps = state => {
  const { loading, annotation } = state
  return {
    loading:state.get('loading'),
    annotation:state.get('annotation')
  }
}

export default connect(mapStateToProps)(AnnotationAppContainer)
