/*
 * PROGRAMMING 2 – MACHINE PROBLEM
 * University of Perpetual Help System DALTA – Molino Campus
 * BS Information Technology - Game Development
 * Dataset: https://www.kaggle.com/datasets/asaniczka/video-game-sales-2024
 *
 * productCategoryProfitability.js  (Node.js)
 *
 * ANALYSIS: Product Category Profitability
 *   - Reads CSV from a user-supplied file path
 *   - Groups records by genre (category)
 *   - Computes total sales and average sale per category
 *   - Identifies most & least profitable categories
 *
 * Run: node productCategoryProfitability.js
 */

'use strict';

const fs       = require('fs');
const path     = require('path');
const readline = require('readline');

// ─────────────────────────────────────────────────────────────────────────────
//  Helper: parse a single CSV line (handles double-quoted fields)
// ─────────────────────────────────────────────────────────────────────────────
function parseCSVLine(line) {
    const cols    = [];
    let   inQuote = false;
    let   current = '';

    for (let i = 0; i < line.length; i++) {
        const ch = line[i];
        if (ch === '"') {
            inQuote = !inQuote;
        } else if (ch === ',' && !inQuote) {
            cols.push(current.trim());
            current = '';
        } else {
            current += ch;
        }
    }
    cols.push(current.trim());
    return cols;
}

// ─────────────────────────────────────────────────────────────────────────────
//  Module: loadDataset(filePath) → Array of record objects
// ─────────────────────────────────────────────────────────────────────────────
function loadDataset(filePath) {
    const rawContent = fs.readFileSync(filePath, 'utf8');
    const lines      = rawContent.split(/\r?\n/).filter(l => l.trim() !== '');

    if (lines.length < 2) {
        throw new Error('CSV file is empty or contains only a header row.');
    }

    // ── Detect column indices from header ──────────────────────────────────
    const headers = parseCSVLine(lines[0]).map(h => h.toLowerCase().replace(/"/g, ''));

    const titleIdx = headers.findIndex(h => ['title','name','game'].includes(h));
    const genreIdx = headers.findIndex(h => ['genre','category'].includes(h));
    const salesIdx = headers.findIndex(h => ['total_sales','sales','totalsales'].includes(h));

    if (genreIdx === -1 || salesIdx === -1) {
        throw new Error(
            `Required columns not found.\n` +
            `Expected: 'genre' (or 'category') and 'total_sales' (or 'sales').\n` +
            `Found headers: ${lines[0]}`
        );
    }

    // ── Parse data rows ────────────────────────────────────────────────────
    const records = [];
    let   skipped = 0;

    for (let i = 1; i < lines.length; i++) {
        const cols  = parseCSVLine(lines[i]);
        const genre = genreIdx < cols.length ? cols[genreIdx].replace(/"/g, '') : '';
        const rawSales = salesIdx < cols.length ? cols[salesIdx].replace(/[",]/g, '') : '';
        const sales  = parseFloat(rawSales);
        const title  = titleIdx >= 0 && titleIdx < cols.length
                     ? cols[titleIdx].replace(/"/g, '') : 'Unknown';

        if (!genre || isNaN(sales)) { skipped++; continue; }
        records.push({ title, genre, sales });
    }

    if (skipped > 0) console.log(`[INFO]  ${skipped} malformed row(s) skipped.`);
    return records;
}

// ─────────────────────────────────────────────────────────────────────────────
//  Module: analyzeByCategory(records) → sorted category stats
// ─────────────────────────────────────────────────────────────────────────────
function analyzeByCategory(records) {
    const statsMap = {};   // { genre: { total, count } }

    for (const rec of records) {
        if (!statsMap[rec.genre]) statsMap[rec.genre] = { total: 0, count: 0 };
        statsMap[rec.genre].total += rec.sales;
        statsMap[rec.genre].count += 1;
    }

    // Convert to array and sort by total sales descending
    return Object.entries(statsMap)
        .map(([category, { total, count }]) => ({
            category,
            totalSales : Math.round(total * 100) / 100,
            avgSale    : Math.round((total / count) * 100) / 100,
            count
        }))
        .sort((a, b) => b.totalSales - a.totalSales);
}

// ─────────────────────────────────────────────────────────────────────────────
//  Module: displayResults(stats, totalRecords)
// ─────────────────────────────────────────────────────────────────────────────
function displayResults(stats, totalRecords) {
    const fmt  = (n) => n.toLocaleString('en-US', { minimumFractionDigits: 2, maximumFractionDigits: 2 });
    const pad  = (s, w) => String(s).padEnd(w);
    const lpad = (s, w) => String(s).padStart(w);
    const rep  = (ch, n) => ch.repeat(n);

    // Grand total for share %
    const grandTotal = stats.reduce((sum, s) => sum + s.totalSales, 0);

    // ================================================================
    //  BANNER
    // ================================================================
    console.log();
    console.log('  ' + rep('*', 72));
    console.log('  *' + rep(' ', 70) + '*');
    console.log('  *     VIDEO GAME SALES 2024  --  CATEGORY PROFITABILITY REPORT       *');
    console.log('  *     University of Perpetual Help System DALTA - Molino Campus      *');
    console.log('  *     BS Information Technology - Game Development                   *');
    console.log('  *' + rep(' ', 70) + '*');
    console.log('  ' + rep('*', 72));

    // ================================================================
    //  DATASET SUMMARY BOX
    // ================================================================
    console.log();
    console.log('  +--------------------------+--------------------------+');
    console.log(`  |  Total Games Loaded      |  ${lpad(totalRecords.toLocaleString(), 24)}  |`);
    console.log(`  |  Unique Categories       |  ${lpad(stats.length, 24)}  |`);
    console.log(`  |  Grand Total Sales (M)   |  ${lpad(fmt(grandTotal), 24)}  |`);
    console.log('  +--------------------------+--------------------------+');

    // ================================================================
    //  MAIN TABLE
    // ================================================================
    console.log();
    console.log('  ' + rep('=', 78));
    console.log(`  ${'RANK'.padEnd(5)}  |  ${'CATEGORY'.padEnd(18)}  |  ${'TOTAL SALES'.padStart(12)}  |  ${'AVG SALE'.padStart(10)}  |  ${'# GAMES'.padStart(7)}  |  ${'SHARE'.padStart(5)}`);
    console.log('  ' + rep('=', 78));

    stats.forEach((s, i) => {
        const share  = ((s.totalSales / grandTotal) * 100).toFixed(1);
        const marker = i % 2 === 0 ? '>' : ' ';
        const rank   = String(i + 1);

        console.log(`  ${marker}${pad(rank, 4)}  |  ${pad(s.category, 18)}  |  ${lpad(fmt(s.totalSales), 12)}  |  ${lpad(fmt(s.avgSale), 10)}  |  ${lpad(s.count, 7)}  |  ${lpad(share + '%', 5)}`);

        // Divider every 5 rows
        if ((i + 1) % 5 === 0 && (i + 1) < stats.length) {
            console.log('  ' + rep('-', 78));
        }
    });

    console.log('  ' + rep('=', 78));

    // ================================================================
    //  FINDINGS PANEL
    // ================================================================
    const best       = stats[0];
    const worst      = stats[stats.length - 1];
    const bestShare  = ((best.totalSales  / grandTotal) * 100).toFixed(1);
    const worstShare = ((worst.totalSales / grandTotal) * 100).toFixed(1);

    console.log();
    console.log('  +--[ FINDINGS ]' + rep('-', 62) + '+');
    console.log('  |' + rep(' ', 76) + '|');

    const bestLine  = `[#1 MOST PROFITABLE]   ${pad(best.category, 18)}   Sales: ${lpad(fmt(best.totalSales), 12)}   Share: ${lpad(bestShare + '%', 5)}`;
    const worstLine = `[#${stats.length} LEAST PROFITABLE]  ${pad(worst.category, 18)}   Sales: ${lpad(fmt(worst.totalSales), 12)}   Share: ${lpad(worstShare + '%', 5)}`;

    console.log(`  |   ${bestLine.padEnd(73)}|`);
    console.log('  |' + rep(' ', 76) + '|');
    console.log(`  |   ${worstLine.padEnd(73)}|`);
    console.log('  |' + rep(' ', 76) + '|');
    console.log('  +' + rep('-', 76) + '+');
    console.log();
}

// ─────────────────────────────────────────────────────────────────────────────
//  File-path prompt loop (uses readline + fs.existsSync)
// ─────────────────────────────────────────────────────────────────────────────
function askFilePath(rl, callback) {
    rl.question('\nEnter dataset file path: ', (inputPath) => {
        const trimmed = inputPath.trim();

        if (!fs.existsSync(trimmed)) {
            console.log('[ERROR] File does not exist. Please try again.');
            askFilePath(rl, callback);
        } else if (!fs.statSync(trimmed).isFile()) {
            console.log('[ERROR] Path does not point to a file. Please try again.');
            askFilePath(rl, callback);
        } else if (!trimmed.toLowerCase().endsWith('.csv')) {
            console.log('[ERROR] File does not appear to be a CSV. Please try again.');
            askFilePath(rl, callback);
        } else {
            console.log(`[OK]    File found: ${path.resolve(trimmed)}`);
            callback(trimmed);
        }
    });
}

// ─────────────────────────────────────────────────────────────────────────────
//  Main entry point
// ─────────────────────────────────────────────────────────────────────────────
function main() {
    console.log();
    console.log('  +-----------------------------------------------------------------+');
    console.log('  |                                                                 |');
    console.log('  |   PROGRAMMING 2  --  Machine Problem                           |');
    console.log('  |   Product Category Profitability Analyzer                      |');
    console.log('  |   Dataset : Video Game Sales 2024  (Kaggle)                    |');
    console.log('  |                                                                 |');
    console.log('  +-----------------------------------------------------------------+');
    console.log();

    const rl = readline.createInterface({
        input  : process.stdin,
        output : process.stdout
    });

    askFilePath(rl, (filePath) => {
        rl.close();

        try {
            // Step 1 – Load dataset
            console.log('\n[INFO]  Loading dataset...');
            const records = loadDataset(filePath);

            if (records.length === 0) {
                console.log('[ERROR] No valid records found in the file.');
                return;
            }

            console.log(`[INFO]  ${records.length.toLocaleString()} records loaded.`);

            // Step 2 – Analyse
            const stats = analyzeByCategory(records);

            // Step 3 – Display
            displayResults(stats, records.length);

        } catch (err) {
            console.error('[ERROR]', err.message);
        }
    });
}

main();
