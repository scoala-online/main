import Container from 'react-bootstrap/Container'
import Row from 'react-bootstrap/Row'
import Col from 'react-bootstrap/Col'

import CardCarousel from './cardCarousel.component'
import InfoColumns from './infoColumns.component'

export default function HomePage() {

  const rowStyle = {
    display: 'flex',
    justifyContent: 'center'
  }

  const cardItems = [{title: "Title 1", sub: "Subtitle 1"}, {title: "Title 2", sub: "Subtitle 2"}, {title: "Title 3", sub: "Subtitle 3"}]

  const mockImgUrl = "https://picsum.photos/300/200"
  const carouselPages = [{quote: "Title1", name: "Subtitle1", pictureURL: mockImgUrl}, {quote: "Title2", name: "Subtitle2", pictureURL: mockImgUrl}, {quote: "Title3", name: "Subtitle3", pictureURL: mockImgUrl}]

  return (
    <Container fluid style={{
      background: '#dddddd'
    }}>
      <Row>
        <Container fluid style={{
          marginTop: '20vh',
          marginBottom: '39vh',
          marginLeft: '29vw',
          marginRight: '29vw'
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
        <Container style={{
          display: 'flex',
          justifyContent: 'center',
          marginTop: '10vh',
          marginLeft: '30vw',
          marginRight: '30vw'
        }}>
          <span style={{
            fontStyle: 'normal',
            fontWeight: 600,
            fontSize: '2rem',
            display: 'flex',
            alignItems: 'center',
            textAlign: 'center'
          }}>
            Ce ne dorim sa obtinem din acest proiect?
          </span>
        </Container>
        <Container fluid style={{
          marginTop: '10vh',
          marginBottom: '20vh',
          marginLeft: '20vw',
          marginRight: '20vw'
        }}>
          <InfoColumns cardItems={cardItems} />
        </Container>
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
