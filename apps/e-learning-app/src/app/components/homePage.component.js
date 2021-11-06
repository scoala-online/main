import React, { Component } from 'react'

import Container from 'react-bootstrap/Container'
import Row from 'react-bootstrap/Row'
import Col from 'react-bootstrap/Col'

import CardCarousel from './cardCarousel.component'
import InfoColumns from './infoColumns.component'

import { withRouter } from 'react-router-dom';

class HomePage extends Component {

  render() {
    const rowStyle = {
      display: 'flex',
      justifyContent: 'center'
    }

    const cardItems = [{title: "Title 1", sub: "Subtitle 1"}, {title: "Title 2", sub: "Subtitle 2"}, {title: "Title 3", sub: "Subtitle 3"}]

    const carouselPages = [{title: "Title", sub: "Subtitle", imageURL: ""}]

    return (
      <Container fluid style={{
        background: '#dddddd'
      }}>
        <Row>
          <Container fluid style={{
            marginTop: '26vh',
            marginBottom: '39vh',
            marginLeft: '13vw',
            marginRight: '13vw'
          }}>
            <Row style={rowStyle}>
              <span style={{
                fontWeight: 'bold',
                fontSize: '3.75rem',
                textAlign: 'center'
              }
              }>
                Scoala Online
              </span>
            </Row>
            <Row style={rowStyle}>
              <span style={{
                fontSize: '1.5rem',
                textAlign: 'center'
              }
              }>
                12 ani de educatie. Un singur site.
              </span>
            </Row>
            <Row style={ {...rowStyle, ...{
              margin: '30px'
            }}}>
              <span style={{
                fontSize: '1.875rem',
                textAlign: 'center'
              }}>
                Invata oriunde vrei, in ritmul tau.
              </span>
            </Row>
            <Row style={ {...rowStyle, ...{
              margin: '30px'
            }}}>
              <span style={{
                fontSize: '1.875rem',
                textAlign: 'center'
              }}>
                Alege profesorul in functie de ce stil de explicatie preferi.
              </span>
            </Row>
          </Container>
        </Row>
        <Row>
          <Col>
            <InfoColumns cardItems={cardItems} />
          </Col>
        </Row>
        <Row>
          <Container fluid style={{
            marginTop: '14vh',
            marginBottom: '17vh',
            marginLeft: '7vw',
            marginRight: '7vw'
          }}>
            <Row style={rowStyle}>
              <CardCarousel pages={carouselPages} />
            </Row>
          </Container>
        </Row>
      </Container>
    );
  }
}

export default withRouter(HomePage);
