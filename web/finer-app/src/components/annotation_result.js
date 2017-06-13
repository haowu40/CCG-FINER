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
import Popover, {PopoverAnimationVertical} from 'material-ui/Popover';
import Chip from 'material-ui/Chip';

const DIVIDER_STYLE = {
  backgroundColor: 'rgb(232, 232, 232)',
  color: 'rgba(0,0,0,0.4)'
};


const TOKEN_SPAN_STYLE = {
  backgroundColor: 'rgb(232, 232, 232)',
  color: 'rgba(0,0,0,0.4)'
};

const ResultStyle = {
  fontSize:20
}

const AnnotationResult = function(args){
  const {value, onClick, selected} = args
  console.log(selected)
  if (Object.keys(value).length > 0){
    let sentence_view = []
    let tokens = value.tokens
    let mentions = value.mentions

    let start2end = new Map()

    let start2mention = new Map()

    _.each(mentions, (mention) => {
      start2end.set(mention.start, mention.end)
      start2mention.set(mention.start, mention)      
    })

    console.log(start2end)

    let i = 0
    let seq_key = 0

    let selected_mention = start2mention.get(selected)


    while(i < value.tokens.length){
        let end = start2end.get(i)
        let isMention = true
        if(end === undefined){
          end = i+1
          isMention = false
        }


        let text = _.slice(tokens, i, end)
        text = text.join(" ")
        let token_span = undefined;
        let explnation_span = undefined;
        if (isMention) {
          let mention = start2mention.get(i)

          let type_names = _.chain(mention.types)
                            .map((t) => t.type_name)
                            .filter((t) => t !== undefined)
                            .map((t) => <span className="type-annotation"> {t} </span>)
                            .value()
          console.log(type_names);

          token_span = (<span 
                          onClick={(e) => onClick(mention)}
                          key={seq_key++}>                           
                           [<b> {text} </b> ({type_names}) ]
                        </span>)  
  
          if(i === selected && mention !== undefined){
              // explnation_span = (<span 
              //                     open={true}
              //                     anchorEl={token_span}
              //                     key={seq_key++}>
              //                     {JSON.stringify(_.map(mention.types, (t) => t.type_name))}
              //                    </span>)
            
          }else{

          }


        }else{
          token_span = (<span key={seq_key++}> {text} </span>)  
        }
        
        sentence_view.push(token_span)
        if (explnation_span != undefined) {
          sentence_view.push(explnation_span)
        }

        i = end
    }
      return (
        <div>
          <br/>
          <Paper zDepth={3} >

          <AppBar
              style={DIVIDER_STYLE}
              title={<span style={DIVIDER_STYLE}>Annotation Result</span>}             
              showMenuIconButton={false}/>
          <br/>
          <div 
          style={ResultStyle}
          >
                    {sentence_view}
          </div>

          </Paper>

        </div>
    );
  }else{
    return (<div></div>)
  }
}

        // <Tabs style={DIVIDER_STYLE}>
        //       <Tab
        //         style={DIVIDER_STYLE}
        //         label="Annotation Results"
        //       >
        //       Tab 1
        //       </Tab>
        //     </Tabs>


AnnotationResult.propTypes = {
  value: PropTypes.object,
  selected: PropTypes.number.isRequired,
  onClick: PropTypes.func.isRequired
}

export default AnnotationResult
