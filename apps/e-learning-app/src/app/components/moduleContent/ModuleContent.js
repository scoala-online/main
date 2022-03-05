import ContentGrid from '../contentGrid/ContentGrid';
import Sidebar from '../moduleSidebar/Sidebar';
import { allVideos } from '../MockData';

/**
 * MODULE PAGE
 *
 * ModuleContent renders:
 * - the Sidebar component (from ./moduleSidebar/Sidebar.js)
 * - the ContentGrid component (from ./ContentGrid.js)
 *
 * Props:
 *
 * - title
 */

const ModuleContent = (props) => {
  const title = props.title;
  return (
    <>
      <Sidebar title={title} />
      <ContentGrid videos={allVideos} />
    </>
  );
};

export default ModuleContent;
