import React, { Component } from 'react'
import Carousel from 'react-bootstrap/Carousel'

import { withRouter } from 'react-router-dom';

class CardCarousel extends Component {

  render() {
    const carouselCardStyle = {
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
            <div style={Object.assign(carouselCardStyle, {background: page.background})}>
              <h1>
                Hello 1
              </h1>
            </div>
            <Carousel.Caption>
              <h3>{page.title}</h3>
              <p>{page.sub}</p>
            </Carousel.Caption>
          </Carousel.Item>
        ))}
      </Carousel>
    )
  }
}

export default withRouter(CardCarousel);
