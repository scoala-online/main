import { calculateTop, calculateBottom } from '../../utilities/SidebarUtil';
import { SidebarContainer } from '../../themes/Containers';
import { LinkButton } from '../../themes/Buttons';

import fontStyle from '../../themes/FontFamilies';


export default function SideBar(props) {
  let size = props.data.length;
  return (
    <SidebarContainer
      type={props.type}
      pos={props.posSubject}
      size={size}
      topVal={calculateTop(props.type, size, props.posSubject)}
      bottomVal={calculateBottom(props.type, size, props.posSubject)}
      active={props.active}
      ref={props.sideBarRef}
    >
      {
        //Creates the list of buttons inside the sidebar based on the provided data
        props.data.map((item, index) => {
          return (
            <LinkButton
              key={index}
              type={props.type}
              first={index == 0}
              last={index + 1 == size}
              onClick={() => props.onClickFunction(index)}
            >
              <span style={{ ...fontStyle.body_semibold, fontSize: props.fontSize }}>
                {item}
              </span>
            </LinkButton>
          );
        })
      }
    </SidebarContainer>
  );
}
