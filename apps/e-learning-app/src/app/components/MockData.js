import lectie1 from '../../assets/tmp/Module/Lectie1.png';
import lectie2 from '../../assets/tmp/Module/Lectie2.png';
import lectie3 from '../../assets/tmp/Module/Lectie3.png';
import lectie4 from '../../assets/tmp/Module/Lectie4.png';
import lectie5 from '../../assets/tmp/Module/Lectie5.png';
import lectie6 from '../../assets/tmp/Module/Lectie6.png';
import lectie7 from '../../assets/tmp/Module/Lectie7.png';

/*
!!! THIS SHOULD BE A TEMPORARY FILE !!!

  Mock data used for filling sidebar content.
  One entry will be either:
  * title type entry
  * subtitle type entry
  
  A title type entry will have big and bold text.
  A subtitle type entry will have normal font and size.
  Both types will require a path to the page with the filtered
  lessons.

*/

export const SidebarTtitles = [
  {
    subtitle: 'Fronda in literatura interbelica',
    path: '',
  },
  {
    subtitle: "Ion Vinea: 'Manifest activist catre tinerime'",
    path: '',
  },
  {
    subtitle: "Tristan Tzara: 'Manifest despre amorul slab si amorul amar'",
    path: '',
  },
  {
    title: 'Limba si comunicare',
    subtitles: [
      {
        subtitle: 'Punctuatia si justificarile ei sintactice si stilistice',
        path: '',
      },
    ],
  },
];

export const allVideos = [
  {
    title: 'Froda in literatura interbelica ',
    thumbnail: lectie1,
    videoLink: 'https://www.youtube.com/watch?v=dQw4w9WgXcQ',
    length: 208,
    channel: {
      name: 'Profesor Badea',
      link: 'https://www.youtube.com/channel/UC9CoOnJkIBMdeijd9qYoT_g',
      image: 'https://picsum.photos/30/30',
    },
  },
  {
    thumbnail: lectie2,
    title: 'Froda in literatura interbelica',
    videoLink: 'https://www.youtube.com/watch?v=dQw4w9WgXcQ',
    length: 208,
    channel: {
      name: 'Profesor Banescu',
      link: 'https://www.youtube.com/channel/UC9CoOnJkIBMdeijd9qYoT_g',
      image: 'https://picsum.photos/30/30',
    },
  },
  {
    thumbnail: lectie3,
    title: 'Froda in literatura interbelica ',
    videoLink: 'https://www.youtube.com/watch?v=dQw4w9WgXcQ',
    length: 208,
    channel: {
      name: 'Profesor Isabela',
      link: 'https://www.youtube.com/channel/UC9CoOnJkIBMdeijd9qYoT_g',
      image: 'https://picsum.photos/30/30',
    },
  },
  {
    thumbnail: lectie4,
    title: "Ion Vinea: 'Manifest activist  catre tinerime'",
    videoLink: 'https://www.youtube.com/watch?v=dQw4w9WgXcQ',
    length: 208,
    channel: {
      name: 'Profesor Badea',
      link: 'https://www.youtube.com/channel/UC9CoOnJkIBMdeijd9qYoT_g',
      image: 'https://picsum.photos/30/30',
    },
  },
  {
    thumbnail: lectie5,
    title: "Tristea Tzara: 'Manifet despe amorul slab si amorul amar'",
    videoLink: 'https://www.youtube.com/watch?v=dQw4w9WgXcQ',
    length: 208,
    channel: {
      name: 'Profesor Badea',
      link: 'https://www.youtube.com/channel/UC9CoOnJkIBMdeijd9qYoT_g',
      image: 'https://picsum.photos/30/30',
    },
  },
  {
    thumbnail: lectie6,
    title: 'Punctuatia si justificarile ei sintactice si stilistice',
    videoLink: 'https://www.youtube.com/watch?v=dQw4w9WgXcQ',
    length: 208,
    channel: {
      name: 'Profesor Badea',
      link: 'https://www.youtube.com/channel/UC9CoOnJkIBMdeijd9qYoT_g',
      image: 'https://picsum.photos/30/30',
    },
  },
  {
    thumbnail: lectie7,
    title: 'Punctiatia si justificarile ei sintactice si stilistice',
    videoLink: 'https://www.youtube.com/watch?v=dQw4w9WgXcQ',
    length: 208,
    channel: {
      name: 'Profesor Badea',
      link: 'https://www.youtube.com/channel/UC9CoOnJkIBMdeijd9qYoT_g',
      image: 'https://picsum.photos/30/30',
    },
  },
];

export const Lectures = [
  {
    id: 1,
    title: "George Bacovia: 'Rar'",
  },
  {
    id: 2,
    title: "George Bacovia: 'De iarna'",
  },
  {
    id: 3,
    title: "Tudor Arghezi: 'Testament'",
  },
  {
    id: 4,
    title: "Tudor Arghezi: 'Fatalul'",
  },
  {
    id: 5,
    title: "Ion Pillat: 'Aci sosi pe vremuri'",
  },
  {
    id: 6,
    title: "Nu mai : 'Am chef sa gasesc opere'",
  },
  {
    id: 7,
    title: "O sa scriu: 'ce mi vine mie prin cap'",
  },
  {
    id: 8,
    title: 'Aici este o opera',
  },
  {
    id: 9,
    title: 'Aici este o opera 2',
  },
  {
    id: 10,
    title: 'Aici este o opera 3',
  },
];
