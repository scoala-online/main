import { useParams } from 'react-router-dom';

import style from './IndexPageStyles';
import { spacer } from '../../themes/Containers';
import { Lectures } from '../../components/MockData';


const IndexPage = (props) => {
  // Path Parameters
  const { grade, subject } = useParams();

  const getLectureParagraphs = (amount) => {
    return Lectures.map((lecture, index) => {
      return (
        <li>
          <p>lecture.value</p>
        </li>
      );
    });
  };

  return (
    <>
      <div class="filter-container" style={style.filterContainer}>
        <span>Grade: {grade}</span>
        <span class="spacer" style={spacer(4, 'vw')}></span>
        <span>Subject: {subject}</span>
      </div>
      <div class="content-container">
        {getLectureParagraphs()}
      </div>
    </>
  );
};

export default IndexPage;
