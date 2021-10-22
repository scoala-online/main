import { calculateTop, calculateBottom } from '../../utilities/SidebarUtil';
import { SidebarContainer } from '../../themes/Containers';
import { SidebarButton } from '../../themes/Buttons';
import fontStyle from '../../themes/FontFamilies';

/**
 * SideBar renders:
 * - a list of buttons on the left side of the page
 * Props:
 * - type: string ( the type of sidebar ( 'grade' / 'subject' ) )
 * - date: list ( the list of names for the buttons )
 * - subjectPos: number ( position of the first element of the sidebar )
 * - active: boolean ( the sidebar's visibility )
 * - sideBarRef: object ( ref for the sidebar )
 * - onClickFunction function ( function for the onClick property of the buttons in the list )
 * - dimensions: object ( contains the current height and width of the viewport )
 * - fontSize: string ( font-size for the buttons' text )
 */
export default function SideBar(props) {
  // Props
  const itemsAmount = props.data.length;

  return (
    <SidebarContainer
      type={props.type}
      pos={props.subjectPos}
      topVal={calculateTop(
        props.type,
        itemsAmount,
        props.subjectPos,
        props.dimensions.height
      )}
      bottomVal={calculateBottom(
        props.type,
        itemsAmount,
        props.subjectPos,
        props.dimensions.height
      )}
      windowWidth={props.dimensions.width}
      active={props.active}
      ref={props.sideBarRef}
    >
      {props.data.map((item, index) => {
        return (
          <SidebarButton
            key={index}
            type={props.type}
            last={Number(index) + 1 === Number(itemsAmount)}
            windowHeight={props.dimensions.height}
            onClick={() => props.onClickFunction(index)}
          >
            <span
              style={{ ...fontStyle.body_semibold, fontSize: props.fontSize }}
            >
              {item}
            </span>
          </SidebarButton>
        );
      })}
    </SidebarContainer>
  );
}
