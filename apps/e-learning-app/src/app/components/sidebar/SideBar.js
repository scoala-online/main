import { calculateTop, calculateBottom } from '../../utilities/SidebarUtil';
import { SidebarContainer } from '../../themes/Containers';
import { SidebarButton } from '../../themes/Buttons';
import fontStyle from '../../themes/FontFamilies';

export default function SideBar(props) {
  const itemsAmount = props.data.length;

  return (
    <SidebarContainer
      type={props.type}
      pos={props.posSubject}
      topVal={calculateTop(props.type, itemsAmount, props.posSubject)}
      bottomVal={calculateBottom(props.type, itemsAmount, props.posSubject)}
      active={props.active}
      ref={props.sideBarRef}
    >
      {
        //Creates the list of buttons inside the sidebar based on the provided data
        props.data.map((item, index) => {
          return (
            <SidebarButton
              key={index}
              type={props.type}
              last={Number(index) + 1 === Number(itemsAmount)}
              onClick={() => props.onClickFunction(index)}
            >
              <span style={{ ...fontStyle.body_semibold, fontSize: props.fontSize }}>
                {item}
              </span>
            </SidebarButton>
          );
        })
      }
    </SidebarContainer>
  );
}
