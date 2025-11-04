import Link from '@docusaurus/Link';
import useDocusaurusContext from '@docusaurus/useDocusaurusContext';
import Heading from '@theme/Heading';
import Layout from '@theme/Layout';
import clsx from 'clsx';
import type { ReactNode } from 'react';
import React from 'react';

import styles from './index.module.css';

function HomepageHeader() {
  const { siteConfig } = useDocusaurusContext();
  return (
    <header className={clsx(styles.heroBanner)}>
      <div className={clsx('container', styles.centeredContainer)}>
        <Heading as='h1' className={clsx('hero__title', styles.title)}>
          {siteConfig.title}
        </Heading>
        <p className={clsx('hero__subtitle', styles.subtitle)}>
          {siteConfig.tagline}
        </p>
        <p className={clsx('hero__description', styles.heroDescription)}>
          Welcome to my personal learning app! Here you'll find my curated
          notes, diagrams, and code implementations for system design, low-level
          design, and more. This site is a living portfolio of my journey to
          master software engineering concepts and best practices.
        </p>
        <div className={clsx(styles.socialLinks, styles.socialLinksAligned)}>
          <Link
            className={clsx(
              'button button--primary button--lg',
              styles.notesButton
            )}
            to='/docs/A-intro'
          >
            <span role='img' aria-label='Notes' style={{ marginRight: '8px' }}>
              📚
            </span>
            Go to My Notes
          </Link>
          <Link
            className={clsx(
              'button button--info button--lg',
              styles.socialButton
            )}
            to='https://www.linkedin.com/in/ayush-kumar-shukla/'
            target='_blank'
            rel='noopener noreferrer'
          >
            <span
              role='img'
              aria-label='LinkedIn'
              style={{ marginRight: '8px' }}
            >
              🔗
            </span>
            LinkedIn
          </Link>
          <Link
            className={clsx(
              'button button--info button--lg',
              styles.socialButton
            )}
            to='https://github.com/Ayush-k-Shukla'
            target='_blank'
            rel='noopener noreferrer'
          >
            <span role='img' aria-label='GitHub' style={{ marginRight: '8px' }}>
              💻
            </span>
            GitHub
          </Link>
          <Link
            className={clsx(
              'button button--secondary button--lg',
              styles.socialButton
            )}
            to='https://drive.google.com/file/d/16c_3rey3mkGTxxAQputdnWOmWG3sHZZo/view'
            target='_blank'
            rel='noopener noreferrer'
          >
            <span role='img' aria-label='Resume' style={{ marginRight: '8px' }}>
              📄
            </span>
            My Resume
          </Link>
        </div>
      </div>
    </header>
  );
}

export default function Home(): ReactNode {
  const { siteConfig } = useDocusaurusContext();
  return (
    <Layout
      title={`Hello from ${siteConfig.title}`}
      description='A personal learning app and portfolio for system design, LLD, and software engineering concepts.'
    >
      <HomepageHeader />
    </Layout>
  );
}
