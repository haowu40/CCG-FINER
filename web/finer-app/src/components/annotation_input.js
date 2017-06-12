import React from 'react'
import RaisedButton from 'material-ui/RaisedButton';
import TextField from 'material-ui/TextField'
import PropTypes from 'prop-types'
import LinearProgress from 'material-ui/LinearProgress';
import RefreshIndicator from 'material-ui/RefreshIndicator';


class AnnotationInput extends React.Component {

  static propTypes = {
    value: PropTypes.string.isRequired,
    onClick: PropTypes.func.isRequired,
    lock: PropTypes.bool.isRequired
  }

  render() {
    if (this.props.lock){


      return <div>
        <TextField 
          ref="annotatioInputField"
          floatingLabelText="Put your document here."    
          defaultValue={this.props.value}
          multiLine={true}
          fullWidth={true}
          rows={10} />

        <LinearProgress mode="indeterminate" />
        <RaisedButton 
          label="Processing" 
          disabled={true} 
          fullWidth={true}
          onClick={e => this.props.onClick(this.refs.annotatioInputField.getValue())}/>

        <LinearProgress mode="indeterminate" />

      </div>
    }else{
      return (
      <div>
        <TextField 
          ref="annotatioInputField"
          floatingLabelText="Put your document here."    
          multiLine={true}
          fullWidth={true}
          defaultValue={this.props.value}
          rows={10} />
        <br/>
        <RaisedButton 
          label="Annotate" 
          secondary={true} 
          fullWidth={true}
          onClick={e => this.props.onClick(this.refs.annotatioInputField.getValue())}/>
      </div>
       );
      }
    }
    
}

export default AnnotationInput
