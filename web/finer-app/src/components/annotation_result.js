import React from 'react'
import PropTypes from 'prop-types'
import _ from "lodash"
import AppBar from 'material-ui/AppBar';
import {Card, CardActions, CardHeader, CardMedia, CardTitle, CardText} from 'material-ui/Card';
import Divider from 'material-ui/Divider';
import Paper from 'material-ui/Paper';
import {Tabs, Tab} from 'material-ui/Tabs';
import FontIcon from 'material-ui/FontIcon';
import MapsPersonPin from 'material-ui/svg-icons/maps/person-pin';
import INIT_ANNOTATION from "../reducers"

const DIVIDER_STYLE = {
  backgroundColor: 'rgb(232, 232, 232)',
  color: 'rgba(0,0,0,0.4)'
};



const AnnotationResult = function(args){
  const {value, onClick} = args
  console.log(args)

  console.log(!Object.keys(value).length)

  if (Object.keys(value).length > 0){

    console.log(value)

    // let sentence_view = []
    // let tokens = value.tokens

    // for (var i = 0; i < value.tokens.length ; i++) {
    //   sentence_view.push((<span> {tokens[i]} </span>))
    // }

    // console.log(value.tokens)
    // console.log(_.map)
      return (
        <div>
           <Tabs style={DIVIDER_STYLE}>
              <Tab
                style={DIVIDER_STYLE}
                label="Annotation Results"
              >
              Tab 1
              </Tab>
            </Tabs>

        </div>
    );
  }else{
    return (<div></div>)
  }
}



AnnotationResult.propTypes = {
  value: PropTypes.object,
  onClick: PropTypes.func.isRequired
}

export default AnnotationResult
