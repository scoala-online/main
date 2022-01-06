import './Sidebar.css';
import { Link } from 'react-router-dom';
import { MockData } from '../MockData.js';
import { BiArrowBack } from 'react-icons/bi';

import style from './SidebarStyles.js';

/**
 * MODULE PAGE
 * 
 * Sidebar renders:
 * - a list of buttons on the left side of the page
 * - a button that goes back to 'Content Page'
 * 
 * Props:
 * - title: string (the name of the title that will appear on the top of the sidebar)
 */
function Sidebar(props) {
  const { title } = props;
  //Style Variables
  
  return (
    <>
      <Link to="#" style={style.nav_back_style}>
          <BiArrowBack />
          &nbsp; <span style={{ fontSize: '1rem' }}>inapoi la capitole</span>
      </Link>
      <nav style={style.nav_style}>
        <p style={{ ...style.nav_p, ...style.title_style }}>{title}</p>
        <ul style={style.ul_style} >
          {MockData.map((item) => {
            while (item.title != null) {
              const listSubtitles = item.subtitles.map((subtitle) => (
                <Link style={style.sidebar_item_link} to={subtitle.path}>
                  <li>{subtitle.subtitle}</li>
                </Link>
              ));
              return (
                <>
                  <p style={{ ...style.nav_p, ...style.sub_title_style }}>{item.title}:</p>
                  {listSubtitles}
                </>
              );
            }
            return (
              <>
                <Link style={style.sidebar_item_link} to={item.path}>
                  <li>{item.subtitle}</li>
                </Link>
              </>
            );
          })}
        </ul>
      </nav>
      <main style={style.main_style}></main>
    </>
  );
}
export default Sidebar;
