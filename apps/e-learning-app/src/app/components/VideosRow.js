import Video from './video/Video';
import {Grid, Row,Col} from 'react-bootstrap';
import 'bootstrap/dist/css/bootstrap.min.css';

const VideosRow = (props) => {
  const { videos } = props;
  const video_row_container_style = {
    backgroundColor: '#fff',
    marginBottom: '20px',
    display: 'flex',
    flexwrap: 'wrap',
    height: '100%',
    width: '100vw',
    transition: '.5s ease-in-out',
  };
  const videos_label_container_style = {
    padding: '10px 20px 10px 0',
  };
  const video_row_item = {
    display: 'flex',
    flexWrap: 'wrap',
    height: '100%',
  };
  return (
    <div style={video_row_container_style}>
       <div style={videos_label_container_style}>
        <div style={videos_label_container_style}></div>
       <div style={video_row_item}>
            <Row lg="auto" >
            {videos.map((video) => (
              <Col>
                <Video video={video} />
              </Col>
            ))}
            </Row>
         </div>
       </div> 
     </div>
  );
};

export default VideosRow;
