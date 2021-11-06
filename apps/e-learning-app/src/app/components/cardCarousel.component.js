import React, { Component } from 'react'
import Carousel from 'react-bootstrap/Carousel'

import { withRouter } from 'react-router-dom';

class CardCarousel extends Component {

  render() {
    const carouselCardStyle = {
      background: '#446677',
      width: '52vw',
      height: '51vh'
    }

    return (
      <Carousel style={{
        width: '52vw',
        height: '51vh'
      }}>
        {this.props.pages.map(page => (
          <Carousel.Item>
            <div style={carouselCardStyle}>
              <h1>
                Hello 1
              </h1>
            </div>
            <Carousel.Caption>
              <h3>First slide label</h3>
              <p>Nulla vitae elit libero, a pharetra augue mollis interdum.</p>
            </Carousel.Caption>
          </Carousel.Item>
        ))}
      </Carousel>
    )
  }
}

export default withRouter(CardCarousel);
