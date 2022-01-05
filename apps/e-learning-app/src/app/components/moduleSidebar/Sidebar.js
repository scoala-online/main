import './Sidebar.css';
import { Link } from 'react-router-dom';
import { SidebarData } from './SidebarData.js';
import { BiArrowBack } from 'react-icons/bi';
import { navBarHeight, minNavBarHeight } from '../../themes/Sizes.js';
function Sidebar(props) {
  const { title } = props;
  //Style Variables
  const main_style = {
    transition: '.5s ease-in-out',
  };
  const ul_style = {
    display: 'block',
    listStyleType: 'disc',
    marginBlockStart: '0em',
    marginBlockEnd: '0em',
    marginInlineStart: '0px',
    marginInlineEnd: '0px',
    paddingInlineStart: '0px',
  };
  const nav_style = {
    
    fontFamily: 'Open Sans, sans-serif',
    fontSize: '18px',
    background: 'rgb(189,189,189)',
    bottom: '0',
    'box-shadow': '0 0 5px #666',
    height: '70%',
    'padding-top': '38px',
    'margin-top': `calc(max(${navBarHeight}vh,${minNavBarHeight}px) + 68px)` ,
    position: 'fixed',
    float:'left',
    'overflow-y':"auto",
    'overflow-x':'hidden',
    display: 'block',
    top: '0',
    left: '0',
    transition: '.5s ease-in-out',
    width: '17vw',
    minWidth: '250px',
  };
  const nav_p = {
    color: '#000',
    display: 'block',
    padding: '12px 16px',
    transition: '.5s',
    'text-decoration': 'none',
  };
  const sidebar_item_link = {
    color: '#000',
    display: 'block',
    padding: '12px 47px',
    transition: '.5s',
    'text-decoration': 'none',
    'word-wrap': 'break-word',
  };
  const title_style = {
    fontSize: '24px',
    fontWeight: 700,
    wordWrap: 'break-word',
    marginBlockStart: '0em',
    marginBlockEnd: '0em',
    marginInlineStart: '0px',
    marginInlineEnd: '0px',
  };

  const sub_title_style = {
    fontWeight: 700,
    marginBlockStart: '0em',
    marginBlockEnd: '0em',
    marginInlineStart: '0px',
    marginInlineEnd: '0px',
  };
  const nav_back_style = {
    padding: '8px 0px',
    position: 'fixed',
    float:'left',
    left: '2px',
    top: 0,
    fontSize: '20px',
    textAlign: 'center',
    color: '#000',
    backgroundColor: 'rgb(242,242,242)',
    fontWeight: '600',
    textDecoration: 'none',
    wordWrap: 'breakWord',
    display: 'flex',
    justifyContent: 'center',
    alignItems: 'center',
    width: '17vw',
    minWidth: '250px',
    height:'68px',
    'margin-top': `calc(max(${navBarHeight}vh,${minNavBarHeight}px))` ,

  };
  const nav_expand_style = {
    transitionDelay: '.25s',
  };
  return (
    <>
      <Link to="#" style={nav_back_style}>
          <BiArrowBack />
          &nbsp; <span style={{ fontSize: '1rem' }}>inapoi la capitole</span>
      </Link>
      <nav style={nav_style}>
        <p style={{ ...nav_p, ...title_style }}>{title}</p>
        <ul style={ul_style} >
          {SidebarData.map((item) => {
            while (item.title != null) {
              const listSubtitles = item.subtitles.map((subtitle) => (
                <Link style={sidebar_item_link} to={subtitle.path}>
                  <li>{subtitle.subtitle}</li>
                </Link>
              ));
              return (
                <>
                  <p style={{ ...nav_p, ...sub_title_style }}>{item.title}:</p>
                  {listSubtitles}
                </>
              );
            }
            return (
              <>
                <Link style={sidebar_item_link} to={item.path}>
                  <li>{item.subtitle}</li>
                </Link>
              </>
            );
          })}
        </ul>
      </nav>
      <main style={main_style}></main>
    </>
  );
}
export default Sidebar;
