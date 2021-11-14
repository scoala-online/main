import Carousel from 'react-bootstrap/Carousel'

export default function CardCarousel(props) {
  // Props
  const pages = props.pages;

  const carouselCardStyle = {
    width: '52vw',
    height: '51vh'
  }

  return (
    <Carousel style={{
      width: '52vw',
      height: '51vh'
    }}>
      {pages.map(page => (
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
