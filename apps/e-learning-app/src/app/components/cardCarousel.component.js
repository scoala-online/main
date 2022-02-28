import { useRef, useState, useEffect } from 'react';

import Carousel from 'react-bootstrap/Carousel';
import Button from 'react-bootstrap/Button';
import Image from 'react-bootstrap/Image';

import Container from 'react-bootstrap/Container';
import Row from 'react-bootstrap/Row';
import Col from 'react-bootstrap/Col';

export default function CardCarousel(props) {
  // Props
  const pages = props.pages;

  // States
  const [pageIndex, setPageIndex] = useState(0);
  const [direction, setDirection] = useState(null);
  const [carouselItemCount, setCarouselItemCount] = useState(pages.length);

  // Styles
  const cardStyle = {
    width: '52vw',
    height: '51vh',
    background: '#E0E0E0',
  };

  const userImageStyle = {
    width: '16.7vw',
    height: '29.7vh',
  };

  const textStyle = {
    fontSize: '1.5rem',
    fontStyle: 'normal',
    fontWeight: 'normal',
    textAlign: 'center',
    verticalAlign: 'center',
  };

  const circleButtonStyle = {
    width: '80px',
    height: '80px',
    background: '#828282',
  };

  const innerColumnStyle = {
    height: '51vh',
  };

  // Utility

  // Method called to handle toggle (next/prev)
  function toggleCarousel(direction) {
    const [min, max] = [0, carouselItemCount - 1];
    var index = pageIndex;

    if (direction === 'next') {
      index = pageIndex + 1;
    } else if (direction === 'prev') {
      index = pageIndex - 1;
    }

    if (index > max) {
      // at max, start from top
      index = 0;
    }

    if (index < min) {
      // at min, start from max
      index = max;
    }

    setDirection(direction);
    setPageIndex(index);
  }

  // Event handlers
  const handlePrevButtonClick = () => toggleCarousel('prev');
  const handleNextButtonClick = () => toggleCarousel('next');

  // Parameters: quote, name, pictureURL
  return (
    <Container fluid>
      <Row className="justify-content-center align-items-center text-center">
        <Col>
          <Image
            roundedCircle
            src="../../assets/arrow_prev.svg"
            style={circleButtonStyle}
            onClick={handlePrevButtonClick}
          />
        </Col>
        <Col md="auto">
          <Carousel
            indicators={false}
            controls={false}
            activeIndex={pageIndex}
            direction={direction}
            style={cardStyle}
          >
            {pages.map((page) => (
              <Carousel.Item>
                <Container fluid style={cardStyle}>
                  <Row className="justify-content-center align-items-center text-center">
                    <Col
                      className="border d-flex align-items-center justify-content-center"
                      style={innerColumnStyle}
                    >
                      <Image src={page.pictureURL} style={userImageStyle} />
                    </Col>
                    <Col
                      className="border d-flex align-items-center justify-content-center"
                      style={innerColumnStyle}
                    >
                      <div style={textStyle}>
                        "{page.quote}" - {page.name}
                      </div>
                    </Col>
                  </Row>
                </Container>
              </Carousel.Item>
            ))}
          </Carousel>
        </Col>
        <Col>
          <Image
            roundedCircle
            src="../../assets/arrow_next.svg"
            style={circleButtonStyle}
            onClick={handleNextButtonClick}
          />
        </Col>
      </Row>
    </Container>
  );
}
