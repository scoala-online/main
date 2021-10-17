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
        <Carousel.Item>
          <div style={carouselCardStyle}>
            <h1>
              Hello 2
            </h1>
          </div>

          <Carousel.Caption>
            <h3>Second slide label</h3>
            <p>Lorem ipsum dolor sit amet, consectetur adipiscing elit.</p>
          </Carousel.Caption>
        </Carousel.Item>
        <Carousel.Item>
          <div style={carouselCardStyle}>
            <h1>
              Hello 3
            </h1>
          </div>

          <Carousel.Caption>
            <h3>Third slide label</h3>
            <p>Praesent commodo cursus magna, vel scelerisque nisl consectetur.</p>
          </Carousel.Caption>
        </Carousel.Item>
      </Carousel>
    )
  }
}

export default withRouter(CardCarousel);
