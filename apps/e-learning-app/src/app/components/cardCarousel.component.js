import { useRef, useState, useEffect } from 'react';

import Carousel from 'react-bootstrap/Carousel'
import Button from 'react-bootstrap/Button'
import Image from 'react-bootstrap/Image'

import Container from 'react-bootstrap/Container'
import Row from 'react-bootstrap/Row'
import Col from 'react-bootstrap/Col'

export default function CardCarousel(props) {
  // Props
  const pages = props.pages;

  // States
  const [index, setIndex] = useState(0);
  const [direction, setDirection] = useState(null);
  const [carouselItemCount, setCarouselItemCount] = useState(pages.length);

  // Styles
  const carouselCardStyle = {
    width: '52vw',
    height: '51vh'
  }

  const circleButtonStyle = {
    width: '80px',
    height: '80px',
    background: '#828282'
  }

  const userImageStyle = {
    width: '16.7vw',
    height: '29.7vh'
  }

  // Utility

  // Method called to handle toggle (next/prev)
  function toggleCarousel(direction) {
    const [min, max] = [0, carouselItemCount - 1]

    if (direction === 'next') {
      setIndex(index + 1)
      console.log("Index Next: " + index)
    }
    else if (direction === 'prev') {
      setIndex(index - 1)
      console.log("Index Prev: " + index)
    }

    if (index > max) {
      // at max, start from top
      setIndex(0)
    }

    if (index < min) {
      // at min, start from max
      setIndex(max)
    }

    setDirection(direction)
    setIndex(index)
  }

  // Event handlers
  const handlePrevButtonClick = () => toggleCarousel("prev")
  const handleNextButtonClick = () => toggleCarousel("next")

  // Parameters: quote, name, pictureURL
  return (
    <Container>
      <Row>
        <Col>
          <Image roundedCircle src='../../assets/arrow_prev.svg' style={circleButtonStyle} onClick={handlePrevButtonClick} />
        </Col>
        <Col>
          <Carousel indicators={false} controls={false} activeIndex={index} direction={direction}>
            {pages.map(page => (
              <Carousel.Item>
                <Container fluid style={Object.assign(carouselCardStyle, { background: "#E0E0E0" })}>
                  <Row className="align-items-center">
                    <Col style={{}}>
                      <Image src={page.pictureURL} style={userImageStyle} />
                    </Col>
                    <Col>
                      <div style={{ textAlign: 'center', verticalAlign: 'center' }}>
                        {page.quote} - {page.name}
                      </div>
                    </Col>
                  </Row>
                </Container>
              </Carousel.Item>
            ))}
          </Carousel>
        </Col>
        <Col>
          <Image roundedCircle src='../../assets/arrow_next.svg' style={circleButtonStyle} onClick={handleNextButtonClick} />
        </Col>
      </Row>
    </Container>
  )
}
